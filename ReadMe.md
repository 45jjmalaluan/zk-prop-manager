## ZooKeeper data nodes 

#### Manual `znode` creation

```
create /properties "property path"
create /properties/validation "validation path"
```

E.g. Validation path structure 
```
/properties/validation/personal
/properties/validation/contact
/properties/validation/address
```

## Running with Spring Boot

`./gradlew clean bootRun`

## API

#### POST

`http://localhost:8054/validation/properties`

Request Body:
```
{
  "node": "personal",
  "dataPair": [
    {
      "key": "name",
      "regExp": "[A-Z][a-z]*( [A-Z][a-z]*)*"
    },
    {
      "key": "spouse_name",
      "regExp": "[A-Z][a-z]*( [A-Z][a-z]*)*"
    }
  ]
}
```

#### GET

`http://localhost:8054/properties/validation/personal`

#### DELETE

`http://localhost:8054/properties/validation/personal`

#### GET

`http://localhost:8054/properties/validation/file`


