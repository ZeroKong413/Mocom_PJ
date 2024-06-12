const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const port = 3000;

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});


const mysql = require('mysql2');
const config = require('./config.json');
const bcrypt = require('bcrypt');

const saltRounds = 10; // bcrypt 솔트 라운드
app.use(express.json()); // JSON 요청 본문을 파싱하기 위해

const connection = mysql.createConnection({
    host : config.development.host,
    user : config.development.username,
    password : config.development.password,
    database : config.development.database
});
// 데이터베이스에 연결
connection.connect(function(err) {
    if (err) {
      console.error('데이터베이스 연결 실패: ' + err.stack);
      return;
    }
  
    console.log('데이터베이스 연결 성공. 연결 ID: ' + connection.threadId);
});

app.post('/namecheck', (req, res) => {
  const{ userName } = req.body;
  // 먼저 userName이 데이터베이스에 존재하는지 확인
  connection.query('SELECT * FROM users WHERE userName = ?', [userName], (error, results, fields) => {
    if (error) {
        return res.status(400).send('서버 오류');
    }
    if (results.length > 0) {
        // 이미 존재하는 userName
        return res.status(400).send('이미 존재하는 사용자입니다.');
    } else{
      res.status(200).send('중복 확인 성공');
    }
  })
})  

app.post('/idcheck', (req, res) => {
  const{ userID } = req.body;
  // 먼저 userID가 데이터베이스에 존재하는지 확인
  connection.query('SELECT * FROM users WHERE userID = ?', [userID], (error, results, fields) => {
    if (error) {
        return res.status(400).send('서버 오류');
    }
    if (results.length > 0) {
        // 이미 존재하는 userID
        return res.status(400).send('이미 존재하는 사용자 ID입니다.');
    } else{
      res.status(200).send('중복 확인 성공');
    }
  })
})
  
// 회원가입 API
app.post('/signup', (req, res) => {
    const { userID, userPassword, userName } = req.body;
    bcrypt.hash(userPassword, saltRounds, (err, hash) => {
      if (err) {
        return res.status(400).send('비밀번호 해싱 중 오류 발생');
      } 
      // 해싱된 비밀번호와 함께 사용자 등록
      connection.query('INSERT INTO users (userID, userPassword, userName) VALUES (?, ?, ?)', [userID, hash, userName], (error, results, fields) => {
        if (error) {
          return res.status(400).send('사용자 등록 중 오류 발생');
        }
        res.status(200).send('회원가입 성공');
      });
    });
    
});

// 로그인 API
app.post('/login', (req, res) => {
  const { userID, userPassword } = req.body;

  connection.query(
    'SELECT * FROM users WHERE userID = ?',
    [userID],
    (error, results, fields) => {
      if (error) {
        return res.status(500).json({message: '서버 오류'});
      }

      if (results.length === 0) {
        return res.status(400).json({message: '존재하지 않는 사용자 ID입니다.'});
      }

      const user = results[0];
      const hashedPassword = user.userPassword

      bcrypt.compare(userPassword, hashedPassword, (err, isMatch) => {
        if (err) {
          return res.status(500).json({message: '비밀번호 확인 중 오류 발생'});
        }

        if (!isMatch) {
          return res.status(400).json({message: '비밀번호가 일치하지 않습니다.'});
        }
        res.status(200).json({
          message: '로그인 성공',
          userID: user.userID,
          userName: user.userName
        });
      });
    }
  );
});


