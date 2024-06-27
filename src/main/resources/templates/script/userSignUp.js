document.addEventListener('DOMContentLoaded', function() {
    const signUpBtn = document.getElementById('sign-up-btn');

    signUpBtn.addEventListener('click', function() {
        const email = document.querySelector('input[placeholder="email"]').value;
        const username = document.querySelector('input[placeholder="username"]').value;
        const nickname = document.querySelector('input[placeholder="nickname"]').value;
        const password = document.querySelector('input[placeholder="password"]').value;

        const userData = {
            email: email,
            username: username,
            nickname: nickname,
            password: password
        };

        const url = `http://localhost:8080/api/users`;
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('회원가입에 실패했습니다.');
            }
            return response.json();
        })
        .then(data => {
            alert(`가입을 축하합니다, ${data.nickname}님.`);
            window.location.href = 'logIn.html';
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message);
        });
    });
});