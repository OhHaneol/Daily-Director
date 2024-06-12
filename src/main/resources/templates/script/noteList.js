const switchButton = document.getElementById('switch');
const noteList = document.getElementById('note-list');

// 초기에 미완성 노트 목록을 가져옵니다.
fetchNotes(false);

// 완성/미완성 버튼 클릭 시 처리
switchButton.addEventListener('change', (event) => {
    const isCompleted = event.target.checked;
    fetchNotes(isCompleted);
});

// 백엔드에서 노트 목록 가져오기
function fetchNotes(isCompleted) {
    const url = `http://localhost:8080/api/home/notes?status=${isCompleted}`;
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'X-FP-AUTH-TOKEN': 'eyJhbGciOiJIUzI1NiJ9.eyJuaWNrbmFtZSI6InRlc3RlcjEiLCJ1aWQiOjF9.WNoIH0Tl-tsM6M7uf0YEaOlH8KrmM1ja2o78zs3AHKw'
        }
    })
    .then(response => response.json())
    .then(data => {
        const notes = data.data;
        renderNotes(notes);
    })
    .catch(error => console.error(error));
}

// 노트 목록 렌더링
function renderNotes(notes) {
    noteList.innerHTML = '';
    notes.forEach(note => {
        const tr = document.createElement('tr');
        const td = document.createElement('td');
        td.textContent = note.title;
        tr.appendChild(td);
        noteList.appendChild(tr);
    });
}