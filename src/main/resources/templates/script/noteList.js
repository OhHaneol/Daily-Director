const searchInput = document.getElementById('search-txt');
const searchButton = document.getElementById('search-btn');
const noteList = document.getElementById('note-list');
const switchButton = document.getElementById('switch');
//const token = 'eyJhbGciOiJIUzI1NiJ9.eyJuaWNrbmFtZSI6InRlc3RlcjEiLCJ1aWQiOjF9.WNoIH0Tl-tsM6M7uf0YEaOlH8KrmM1ja2o78zs3AHKw';

// 로컬 스토리지에서 토큰을 가져옵니다.
const token = localStorage.getItem('authToken');

// 토큰이 없으면 로그인 페이지로 리다이렉트
if (!token) {
    window.location.href = '../user/login.html';
}


// 초기에 미완성 노트 목록을 가져옵니다.
fetchNotes(false);

// 완성/미완성 버튼 클릭 시 처리
switchButton.addEventListener('change', (event) => {
    const isCompleted = event.target.checked;
    fetchNotes(isCompleted);
});

// 검색 버튼 클릭 이벤트 리스너
searchButton.addEventListener('click', () => {
    const searchKeyword = searchInput.value.trim();
    if (searchKeyword) {
        fetchSearchResults(searchKeyword);
    } else {
        clearNoteList();
    }
});

// 백엔드에서 노트 목록 가져오기
function fetchNotes(isCompleted) {
    const url = `http://localhost:8080/api/home/notes?status=${isCompleted}`;
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'X-FP-AUTH-TOKEN': token
        }
    })
    .then(response => response.json())
    .then(data => {
        const notes = data.data;
        // 노트를 최근 수정일 또는 생성일 기준으로 정렬
        notes.sort((a, b) => {
            const dateA = a.modifiedAt ? new Date(a.modifiedAt) : new Date(a.createdAt);
            const dateB = b.modifiedAt ? new Date(b.modifiedAt) : new Date(b.createdAt);
            return dateB - dateA;
        });
        renderNotes(notes);
    })
    .catch(error => console.error(error));
}

// 백엔드에서 검색 결과 가져오기
function fetchSearchResults(searchKeyword) {
    const url = `http://localhost:8080/api/home/search?searchKeyword=${encodeURIComponent(searchKeyword)}`;
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'X-FP-AUTH-TOKEN': token
        }
    })
    .then(response => response.json())
    .then(data => {
        const notes = data.data;
        // 검색 결과도 최근 수정일 또는 생성일 기준으로 정렬
        notes.sort((a, b) => {
            const dateA = a.modifiedAt ? new Date(a.modifiedAt) : new Date(a.createdAt);
            const dateB = b.modifiedAt ? new Date(b.modifiedAt) : new Date(b.createdAt);
            return dateB - dateA;
        });
        renderNotes(notes);
    })
    .catch(error => console.error(error));
}

function renderNotes(notes) {
    noteList.innerHTML = '';
    notes.forEach(note => {
        const tr = document.createElement('tr');
        tr.style.cursor = 'pointer'; // 커서를 포인터로 변경하여 클릭 가능함을 나타냄
        tr.addEventListener('click', () => {
            window.location.href = `noteInfo.html?noteId=${note.noteId}`;
        });

        const td = document.createElement('td');

        // 제목 표시
        const titleSpan = document.createElement('span');
        titleSpan.textContent = note.title && note.title.trim() !== '' ? note.title : '(제목 없음)';
        titleSpan.style.fontWeight = 'bold';  // 제목을 굵게 표시
        td.appendChild(titleSpan);

        // 줄바꿈
        td.appendChild(document.createElement('br'));

        // 내용 표시
        const contentSpan = document.createElement('span');
        const content = findFirstNonEmptyContent(note.contents);
        contentSpan.textContent = content ? truncateContent(content, 24) : '(내용 없음)';
        contentSpan.style.color = '#666';  // 내용을 회색으로 표시
        td.appendChild(contentSpan);

        if (note.searchType) {
            const p = document.createElement('p');
            p.textContent = note.searchType;
            p.classList.add('search-type');
            td.appendChild(p);
        }

        tr.appendChild(td);
        noteList.appendChild(tr);
    });
}

// contents 배열에서 첫 번째 비어있지 않은 내용을 찾는 함수
function findFirstNonEmptyContent(contents) {
    if (!Array.isArray(contents)) return null;
    for (let content of contents) {
        if (content && content.trim() !== '') {
            return content.trim();
        }
    }
    return null;
}

// 내용을 지정된 길이로 잘라내는 함수
function truncateContent(content, maxLength) {
    if (content.length <= maxLength) return content;
    return content.substr(0, maxLength) + '...';
}

// 노트 목록 초기화
function clearNoteList() {
    noteList.innerHTML = '';
}