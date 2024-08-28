// 'content-select-btn' 클릭 이벤트 핸들러
const contentSelectBtns = document.querySelectorAll('.content-select-btn');
let currentIndex = 0;

contentSelectBtns.forEach((btn, index) => {
  btn.addEventListener('click', () => {
    currentIndex = index;
    const contentTextarea = document.getElementById(`content-${index}`);
    document.getElementById('content-write').value = contentTextarea.value;
  });
});

// 페이지 나갈 때 자동으로 수정 요청 보내기
window.addEventListener('beforeunload', () => {
  const updatedContents = document.getElementById('content-write').value.split('\n\n');
  // NoteController의 updateNote 메서드 호출
  const url = `http://localhost:8080/api/notes/${noteId}`;
  fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      contents: updatedContents
    })
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Failed to update note');
      }
    })
    .catch(error => console.error(error));
});