document.getElementById('create-note-btn').addEventListener('click', () => {
  window.location.href = 'noteInfo.html';
});

// 로그아웃 기능 추가
document.getElementById('logoutIcon').addEventListener('click', function () {
  if (confirm('로그아웃 하시겠습니까?')) {
    // 로컬 스토리지에서 JWT 토큰 제거
    localStorage.removeItem('authToken');

    // 서버에 로그아웃 요청 보내기 (선택적)

    const url = `http://localhost:8080/api/auth/logout`;
    fetch(url, {
      method: 'POST',
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
      }
    })
    .then(response => {
      if (response.ok) {
        console.log('서버 로그아웃 성공');
      } else {
        console.error('서버 로그아웃 실패');
      }
    })
    .catch(error => {
      console.error('로그아웃 요청 중 오류 발생:', error);
    });

    // 로그인 페이지로 리다이렉트
    window.location.href = '../user/login.html';
  }
});