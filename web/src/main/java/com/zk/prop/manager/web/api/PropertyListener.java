package com.zk.prop.manager.web.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zk.prop.manager.core.ZooKeeperConnection;
import com.zk.prop.manager.core.model.DataPair;
import com.zk.prop.manager.core.model.ValidationModel;
import com.zk.prop.manager.core.service.ValidationPropertyServiceImpl;
import com.zk.prop.manager.core.util.DataUtil;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/properties/validation")
public class PropertyListener {
    @Autowired
    private ZooKeeperConnection zooKeeperConnection;

    @Value("${zookeeper.servers}")
    private String zkConnectString;

    @Autowired
    private ValidationPropertyServiceImpl validationPropertyService;

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HttpEntity<Map<String, String>> api() {
        Map<String, String> outMap = new HashMap<>();
        outMap.put("key", "value");
        return new ResponseEntity<>(outMap, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity create(@Valid @RequestBody ValidationModel model) {
        String node = model.getNode();
        try {
            CuratorFramework client = zooKeeperConnection.openClient(zkConnectString);
            validationPropertyService.create(client, node, model.toJson());
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            zooKeeperConnection.closeClient();
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{node}")
                .buildAndExpand(node).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/{node}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getOne(@PathVariable("node") String znode) {
        Map<String, List<DataPair>> jsonMap = new HashMap<>();
        try {
            CuratorFramework client = zooKeeperConnection.openClient(zkConnectString);
            if (!validationPropertyService.exists(client, znode)) {
                return ResponseEntity.notFound().build();
            }
            String jsonArray = validationPropertyService.getData(client, znode);
            List<DataPair> dataPair = DataUtil.getAsList(jsonArray);
            jsonMap.put("dataPair", dataPair);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            zooKeeperConnection.closeClient();
        }

        return new ResponseEntity<>(jsonMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/{node}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable("node") String znode) {
        try {
            CuratorFramework client = zooKeeperConnection.openClient(zkConnectString);
            if (!validationPropertyService.exists(client, znode)) {
                return ResponseEntity.notFound().build();
            }
            validationPropertyService.delete(client, znode);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            zooKeeperConnection.closeClient();
        }
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity download() {
        InputStream inStream = null;
        try {
            CuratorFramework client = zooKeeperConnection.openClient(zkConnectString);
            if (!validationPropertyService.exists(client, "")) {
                return ResponseEntity.notFound().build();
            }
            File property = validationPropertyService.generateFile(client, "", "validation");
            inStream = new FileInputStream(property);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            zooKeeperConnection.closeClient();
        }
        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(MediaType.TEXT_PLAIN_VALUE)).body(new InputStreamResource(inStream));
    }

}
