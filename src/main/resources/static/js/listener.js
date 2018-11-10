function delete_post(post) {
  var pw = window.prompt("비밀번호를 입력하세요.");
  var formData = new FormData();
  formData.append('password', pw);
  $.ajax({
    url: "/post/"+post,
    type: 'delete',
    processData: false,
    contentType: false,
    data: formData
  }).done(function(data) {
    alert('삭제되었습니다.');
    location.href = '/';
  }).catch(function(data) {
    alert('삭제되었습니다.');
    location.href = '/';
  })
}

function edit_post(post) {
  location.href = '/editor/'+post;
}