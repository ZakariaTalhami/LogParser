# Log Parser: Parstatus
### Overview:
  Parstatus is a log parser that is a tuned specificly to curtain log [format](#format), it is able to parse a file by using its API to specify the log file, the application will load, read and parse that log file into the [database](#), which then can be later retrieved using the Restful APi.
***
### API Documentation
 View the following lin for the [API Documentation](https://documenter.getpostman.com/view/4753964/RWTfyMDF).
***
### Log Format
| Timestamp | level | thread |  | class| message |

#### Javadocs:
  The java documentation can be found on the [github pages](https://zakariatalhami.github.io/LogParser/index.html)
***
#### Database ER diagram:
![Database ER diagram](/docs/DB.png)


### Class Diagram

![Class diagram](/docs/ClassDiagram.png)
