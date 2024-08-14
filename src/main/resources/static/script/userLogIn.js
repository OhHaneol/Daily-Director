document.addEventListener('DOMContentLoaded', function () {
  const logInBtn = document.getElementById('log-in-btn');

  logInBtn.addEventListener('click', function () {
    const email = document.querySelector('input[placeholder="email"]').value;
    const password = document.querySelector(
        'input[placeholder="password"]').value;

    const logInData = {
      email: email,
      password: password
    };

    const url = `http://localhost:8080/api/auth/login`;
    fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(logInData)
    })
    .then(response => response.json())
    .then(responseData => {
      if (responseData.success) {
        console.log('Server response:', responseData);
        if (responseData.data && responseData.data.nickname
            && responseData.data.token) {
          alert(`반갑습니다, ${responseData.data.nickname}님.`);
          localStorage.setItem('authToken', responseData.data.token);
          window.location.href = '../note/home.html';
        } else {
          throw new Error('사용자 정보가 올바르지 않습니다.');
        }
      } else {
        throw new Error(responseData.error.message || '로그인에 실패했습니다.');
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert(error.message);
    });
  });
});