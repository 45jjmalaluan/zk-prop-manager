package com.zk.prop.manager.core

import org.apache.curator.test.TestingServer
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static org.junit.Assert.assertNotNull

@Stepwise
class ZooKeeperConnectionSpec extends Specification {
    @Shared
            connection

    @Shared
    String connectString

    def setupSpec() {
        connection = new ZooKeeperConnection()
        TestingServer server = new TestingServer(2154)
        server.start()
        connectString = server.connectString
    }

    def "Open a zookeeper connection"() {
        when: "passing the connect string"
        def zk = connection.open(connectString)
        then: "expect a zookeeper instance"
        assertNotNull(zk)
    }

    def "Close a zookeeper connection"() {
        when: "closing"
        connection.close()
        then: "no errors"
        assert true
    }

    def "Open a zookeeper connection using Curator"() {
        when: "passing the connect string"
        def client = connection.openClient(connectString)
        then: "expect a curator instance"
        assertNotNull(client)
    }

    def "Close a zookeeper connection using Curator"() {
        when: "closing"
        connection.closeClient()
        then: "successful"
        assert true
    }
}
