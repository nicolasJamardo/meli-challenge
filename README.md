# Meli App

## Build

Run `mvn clean package` to build the project. The build artifacts will be stored in the `target/` directory

## Running tests

Run `mvn clean test` 

## Running application on localhost

Run `mvn spring-boot:run -Dspring-boot.run.profiles=local` to execute this app locally.

## Some considerations

The location identification algorithm was based in the triangulation between the three satelites using their distance to the ship that was recieved. The intersection between the first two satellites was found obtaining two possible locations which were later tested in the third satellite founding the location where all three satellite circunferences matched which was the ships location

[This is the algebra detailed trilateration resolution](http://paulbourke.net/geometry/circlesphere/)

For the message decoder, the latency was considered at the beginning of the message

## API Documentation

### URL

Hosted in AWS: http://melichallenge-env.eba-mgre8b2u.us-east-2.elasticbeanstalk.com/

### Hear messages with distances and emergency message from all satellites

**POST** /topsecret

Example body:
```json
{
    "satellites": [
        {
            "name": "kenobi",
            "distance": 412.3105626,
            "message": [
                "",
                "este",
                "es",
                "un",
                "mensaje"
            ]
        },
        {
            "name": "skywalker",
            "distance": 583.0951895,
            "message": [
                "este",
                "",
                "un",
                "mensaje"
            ]
        },
        {
            "name": "sato",
            "distance": 905.5385138,
            "message": [
                "",
                "",
                "es",
                "",
                "mensaje"
            ]
        }
    ]
}
```

Expected response: 

```json
{
    "location": {
        "x": -400.0,
        "y": 200.0
    },
    "message": "este es un mensaje"
}
```


### Hear messages with distances and emergency message from one given satellite

**POST** /topsecret_split/{satellite_name}

Example body:
```json
{
    "distance": 600,
    "message": [
        "",
        "este",
        "es",
        "un",
        "mensaje"
    ]
}
```

### Get all stored messages and distances from previous heard satellites

**GET** /topsecret_split

Expected response: 

```json
{
    "location": {
        "x": -400.0,
        "y": 200.0
    },
    "message": "este es un mensaje"
}
```


### May the force be with you

