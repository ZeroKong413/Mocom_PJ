const config = require('./config.json');
const mysql = require('mysql2');

/* ... 생략 ... */
const connection = mysql.createConnection({
    host : config.development.host,
    user : config.development.username,
    password : config.development.password,
    database : config.development.database
});

connection.connect(function(err) {
    if (err) {
        throw err;
    } else {
        connection.query("SELECT * FROM User", function (err, rows, fields) {
            console.log(rows);
        })
    }
});

