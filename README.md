# RUNNING

The program need to know about your hsql database
where you want to storage data. For this purpose
you need to create json config file that has the
following structure:
```json
{
    "url": "jdbc:hsqldb:file:testing",
    "user": "andreyshspb",
    "password": "password"
}
```

Now we can run program:
```
./table-creation --config database-config.json \
                 --file students.csv \
                 --table students
```

This command import data from `students.csv` file
to a table called `students` inside database declared
in `database-config.json` file
