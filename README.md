# Scala-play-restful for Scale360

## Start application
    sbt run
  
## Test application
    sbt test
    
## CRUD
##### Parameters

    id: Long
    subject: String
    content: String
    status: Int (0 or 1)
    
### Get All Task "GET /tasks"
##### Example
    curl -X GET http://localhost:9000/tasks 
##### Return
    [
      {
        "id": 1,
        "subject": "Subject1",
        "content": "Content1",
        "status": 0
      },
      {
        "id": 2,
        "subject": "Subject2",
        "content": "Content2",
        "status": 1
      },
      {
        "id": 3,
        "subject": "Subject3",
        "content": "Content3",
        "status": 0
      },
      {
        "id": 4,
        "subject": "Subject4",
        "content": "Content4",
        "status": 1
      }
    ]

### Get Task by ID "GET /tasks/:id"
##### Example
    curl -X GET http://localhost:9000/tasks/1
##### Return
      {
        "id": 1,
        "subject": "Subject1",
        "content": "Content1",
        "status": 0
      }

### Insert Data "POST /tasks"
##### Consume JSON
- subject
- content
- status

##### Example
    
    curl -X POST http://localhost:9000/tasks -H "Content-Type: application/json" -d "{\"subject\": \"Test subject 1\", \"content\": \"Test content 1\", \"status\": 0}"

##### Return 
    "Success"
    
### Update Data

##### Update all data "POST /tasks/:id"
##### Consume JSON
- subject
- content
- status

##### Example
    
    curl -X POST http://localhost:9000/tasks/1 -H "Content-Type: application/json" -d "{\"subject\": \"Test subject 1\", \"content\": \"Test content 1\", \"status\": 0}"
    
##### Update status "PUT /tasks/:id/:status"

##### Example
    
    curl -X PUT http://localhost:9000/tasks/1/1 
    
##### Return 
    "Update successfully"
    
### Delete "DELETE /tasks/:id"
##### Example
    curl -X DELETE http://localhost:9000/tasks/1 
    
##### Return 
    "Delete successfully"
