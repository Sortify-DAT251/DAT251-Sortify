# Connect to the postgreSQL database

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