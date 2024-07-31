## Setting up the database
1. Create a MySQL database, for the purpose of this tutorial, it's called `keikai_tutorial`
2. Create the table, for the purpose of this tutorial, it's called `trades`
3. Populate the table with data

## Running the project

1. Download the project
2. Run the maven wrapper which downloads everything for starting up :
`./mvnw jetty:run`

When you see the following messages:
```
...
[INFO] Started Jetty Server
[INFO] Starting scanner at interval of 5 seconds.

```

Visit http://localhost:8080/tutorial with your browser. After you have finishted playing with Keikai, you can press `Ctrl+c` to stop the server.

* If you populated the database with table, it should show the live data 