# Connect to the PostgreSQL Database

Guide to create the sortify database locally and be able to connect with it through spring boot.

## macOs

Start the postgreSQL service with homebrew

```
brew services start postgresql
```

Connect to the service

```
psql -U $(whoami) -d postgres
```

Check what roles exists

```
\du
```

Create the postgres role if it doesnt exist

```
CREATE ROLE postgres WITH LOGIN SUPERUSER PASSWORD 'password';
```

Verify that the role was created

```
\du
```

Create the database: sortifydb

```
CREATE DATABASE sortifydb;
```

Verify it was created

```
\l
```

Exit the service

```
\q
```

## Windows

Start the postgreSQL service:

```


net start postgresql


```

Connect to the service

```


psql -U postgres -d postgres


```

Check what roles exists

```

\du

```

Create the postgres role if it doesnt exist

```

CREATE ROLE sortify WITH LOGIN SUPERUSER PASSWORD 'sortify';


```

Verify that the role was created

```


\du


```

Create the database: sortifydb

```

CREATE DATABASE sortifydb;

```

Verify it was created

```

\l

```

Exit the service

```

\q

```

### How to start everything (manually)

### How to start everything (autmatically)
