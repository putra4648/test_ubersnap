# Step to Reproduce

- Make sure you have `Java 17` and `Maven` latest version

## API Endpoint

Mostly this endpoints used `form-data` to make POST request

- `/resize`
  | PARAMETER | DESC |
  | ------- | ------ |
  | `width` | must be number |
  | `height` | must be number |
  | `image` | file of image |

  Response :
  Upon success this will return resized image as `bytes` <br />
  If error there will return a message

- `/compress`
  | PARAMETER | DESC |
  | ------- | ------ |
  | `image` | file of image |

  Response :
  Upon success this will return compressed image as `bytes` <br />
  If error there will return a message

- `/convert`
  | PARAMETER | DESC |
  | ------- | ------ |
  | `image` | file of image |

  Response :
  Upon success this will return compressed image as `bytes` <br />
  If error there will return a message

### How to run test

- Run `mvn clean install` this will clean and install required dependecies
- Run `mvn clean verify` to test all 
- Wait few minutes
- In root project, open file `target\site\jacoco\index.html` via browser

### How to run

- Run `mvn clean install` this will clean and install required dependecies
- Run `mvn spring-boot:run`
- Then you can test REST API with your desired tools like Postman instead
