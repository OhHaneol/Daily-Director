// 페이지 로드 시 쿼리 파라미터에서 noteId 가져오기
const urlParams = new URLSearchParams(window.location.search);
const noteId = urlParams.get('noteId');

// 로컬 스토리지에서 토큰을 가져옵니다.
const token = localStorage.getItem('authToken');

// 토큰이 없으면 로그인 페이지로 리다이렉트
if (!token) {
    window.location.href = '../user/login.html';
}

if (noteId) {
    // 기존 노트 수정
    fetchNote(noteId);
} else {
    // 새 노트 생성
    setupNewNote();
}

function fetchNote(noteId) {
  // NoteController 의 getNote 메서드 호출
  const url = `http://localhost:8080/api/notes/${noteId}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'X-FP-AUTH-TOKEN': token
    }
  })
    .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch note');
        }
        return response.json();
      })
    .then(data => {
      // 노트 내용 렌더링
      document.getElementById('title').value = data.data.title || '';    // input 태그의 value 에 출력 가능하게 함.
      console.log("data.title = " + data.data.title);

      const hashtagInputs = document.querySelectorAll('.hashtag');
      if (Array.isArray(data.data.noteHashtagNames) && data.data.noteHashtagNames.length > 0) {
        hashtagInputs.forEach((input, index) => {
          if (index < data.data.noteHashtagNames.length) {
            input.value = data.data.noteHashtagNames[index];
            console.log("noteHashtagNames[" + index + "] = " + data.data.noteHashtagNames[index]);
          }
        });
      }

      const contentTextareas = document.querySelectorAll('.content-write');
      if (Array.isArray(data.data.contents) && data.data.contents.length > 0) {
        contentTextareas.forEach((textarea, index) => {
          if (index < data.data.contents.length) {
            textarea.value = data.data.contents[index];
          }
        });
      }

      document.getElementById('switch').checked = data.data.status;
    })
    .catch(error => console.error(error));
}

function setupNewNote() {
    // 빈 폼 설정
    document.getElementById('title').value = '';
    document.querySelectorAll('.hashtag').forEach(input => input.value = '');
    document.querySelectorAll('.content-write').forEach(textarea => textarea.value = '');
    document.getElementById('switch').checked = false;
}


document.addEventListener('DOMContentLoaded', function() {
    const closeButton = document.getElementById('info-close-btn');
    if (closeButton) {
        closeButton.addEventListener('click', function(event) {
            event.preventDefault(); // 기본 동작 방지
            saveNote()
//            .then(() => {
//                window.location.href = 'home.html'; // 저장 후 홈 화면으로 이동
//            })
            .catch(error => {
                console.error('Failed to save note:', error);
                // 에러 발생 시 사용자에게 알림을 줄 수 있습니다.
//                alert('Failed to save note. Please try again.');
                alert(error);
            });
        });
    }
});

let isSaving = false;

function saveNote() {
    if (isSaving) return Promise.resolve(); // 이미 저장 중이면 즉시 resolve

    isSaving = true;
    return new Promise((resolve, reject) => {
        const title = document.getElementById('title').value;
        const hashtagInputs = document.querySelectorAll('.hashtag');
        const hashtagNames = Array.from(hashtagInputs).map(input => input.value).filter(value => value.trim() !== '');
        const contentTextareas = document.querySelectorAll('.content-write');
        const contents = Array.from(contentTextareas).map(textarea => textarea.value);
        const status = document.getElementById('switch').checked;

        const url = noteId ? `http://localhost:8080/api/notes/${noteId}` : 'http://localhost:8080/api/notes';
        const method = noteId ? 'PUT' : 'POST';

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'X-FP-AUTH-TOKEN': token
            },
            body: JSON.stringify({
                title,
                hashtagNames,
                contents,
                status
                // TODO user 의 id 값을 추가로 넣어야 함. 백엔드도 전반적인 수정 필요! note api 도 끝에 유저 id param 추가.
            })
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(errorData.reason || 'Failed to save note');
                })
            }
            return response.json();
        })
        .then(data => {
            console.log('Note saved successfully:', data);
            resolve(data);
        })
        .catch(error => {
            console.error('Error saving note:', error);
            reject(error);
        })
        .finally(() => {
            isSaving = false;
        });
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const closeButton = document.getElementById('info-close-btn');
    if (closeButton) {
        closeButton.addEventListener('click', function(event) {
            event.preventDefault();
            saveNote()
            .catch(error => {
                console.error('Failed to save note:', error);
                alert(error.message); // 서버에서 받은 오류 메시지를 표시
            });
        });
    }
});

let isUnloading = false;

window.addEventListener('beforeunload', function(event) {
    if (!isUnloading) {
        isUnloading = true;
        event.preventDefault(); // 표준 기반 브라우저에서는 이 메시지가 무시됩니다.
        event.returnValue = ''; // 크롬에서는 이 설정이 필요합니다.

        saveNote().finally(() => {
            isUnloading = false;
        });
    }
});