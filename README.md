
# user-webservice

A Microservice which handles user profile Management



## API Reference

#### Get all users

```http
  GET /api/v1/users
```


#### Get user

```http
  GET /api/v1/users/${id}
```

| Parameter | Type      | Description                       |   
| :-------- | :-------  | :-------------------------------- |
| `id`      | `int`     | **Required**. Id of user to fetch |


#### Create user

```http
  POST /api/v1/users
```

| Parameter     | Type     | Description                                |
| :--------     | :------- | :----------------------------------------  |
| `firstName`   | `String` | **Required**. firstName of user            |
| `lastName`    | `String` | **Required**. lastName of user to fetch    |
| `email`       | `String` | **Required**. email of user to fetch       |
| `phoneNumber` | `String` | **Required**. phoneNumber of user to fetch |

#### Update user

```http
  PUT /api/v1/users/{id}
```

| Parameter     | Type     | Description                                |
| :--------     | :------- | :----------------------------------------  |
| `firstName`   | `String` | **Required**. firstName of user            |
| `lastName`    | `String` | **Required**. lastName of user to fetch    |
| `email`       | `String` | **Required**. email of user to fetch       |
| `phoneNumber` | `String` | **Required**. phoneNumber of user to fetch |

#### Delete user

```http
  DELETE /api/v1/users/${id}
```

| Parameter | Type      | Description                       |   
| :-------- | :-------  | :-------------------------------- |
| `id`      | `int`     | **Required**. Id of user to delete|
## Running Tests only

To run tests, run the following command

```bash
  mvn test
```


## Installation

Install user MS with maven

```bash
  mvn clean install
```
  
## Tech Stack



**Server:** Spring Boot, Postgres


## Deployment

To deploy this project run

```bash
 docker-compose build
 docker-compose up
```


## 🚀 About Me
My name is Anaghesh Muruli. I'm a backend engineer who's passionate about building sustainable products

