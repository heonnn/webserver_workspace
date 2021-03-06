<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<section id=enroll-container>
	<h2>회원 가입 정보 입력</h2>
	<form name="memberEnrollFrm" method="POST">
		<table>
			<tr>
				<th>아이디<sup>*</sup></th>
				<td>
					<input type="text" placeholder="4글자이상" name="memberId" id="_memberId" required>
					<input type="button" value="중복검사" onclick="checkIdDuplicate();" />
					<input type="hidden" id="idValid" value="0" />
					<%-- #idValid 0이면 중복검사 통과 전, 1이면 중복검사 통과 후 --%>
				</td>
			</tr>
			<tr>
				<th>패스워드<sup>*</sup></th>
				<td>
					<input type="password" name="password" id="_password" value="1234" required><br>
				</td>
			</tr>
			<tr>
				<th>패스워드확인<sup>*</sup></th>
				<td>	
					<input type="password" id="passwordCheck" required><br>
				</td>
			</tr>  
			<tr>
				<th>이름<sup>*</sup></th>
				<td>	
				<input type="text"  name="memberName" id="memberName" value="김정헌" required><br>
				</td>
			</tr>
			<tr>
				<th>생년월일</th>
				<td>	
				<input type="date" name="birthday" id="birthday" ><br />
				</td>
			</tr> 
			<tr>
				<th>이메일</th>
				<td>	
					<input type="email" placeholder="abc@xyz.com" name="email" id="email" value="najh@naver.com"><br>
				</td>
			</tr>
			<tr>
				<th>휴대폰<sup>*</sup></th>
				<td>	
					<input type="tel" placeholder="(-없이)01012345678" name="phone" id="phone" value="01052795981" maxlength="11" required><br>
				</td>
			</tr>
			<tr>
				<th>주소</th>
				<td>	
					<input type="text" placeholder="" name="address" id="address"><br>
				</td>
			</tr>
			<tr>
				<th>성별 </th>
				<td>
					<input type="radio" name="gender" id="gender0" value="M">
					<label for="gender0">남</label>
					<input type="radio" name="gender" id="gender1" value="F" >
					<label for="gender1">여</label>
				</td>
			</tr>
			<tr>
				<th>취미 </th>
				<td>
					<input type="checkbox" name="hobby" id="hobby0" value="운동"><label for="hobby0">운동</label>
					<input type="checkbox" name="hobby" id="hobby1" value="등산"><label for="hobby1">등산</label>
					<input type="checkbox" name="hobby" id="hobby2" value="독서"><label for="hobby2">독서</label><br />
					<input type="checkbox" name="hobby" id="hobby3" value="게임"><label for="hobby3">게임</label>
					<input type="checkbox" name="hobby" id="hobby4" value="여행"><label for="hobby4">여행</label><br />
				</td>
			</tr>
		</table>
		<input type="submit" value="가입" >
		<input type="reset" value="취소">
	</form>
</section>
<form name="checkIdDuplicateFrm" action="<%= request.getContextPath() %>/member/checkIdDuplicate">
	<input type="hidden" name="memberId" />
</form>
<script>
const checkIdDuplicate = () => {
	const title = "chechIdDuplicatePopup";
	const spec = "width=300px, height=200px";
	const popup = open("", title, spec);
	
	const frm = document.checkIdDuplicateFrm;
	frm.target = title;
	frm.memberId.value = _memberId.value;
	frm.submit();
};
passwordCheck.onblur = () => {
	if(_password.value !== passwordCheck.value) {
		alert("비밀번호가 일치하지 않습니다.");
		_password.select();
		return false;
	}
	return true;
}

document.memberEnrollFrm.onsubmit = () => {
	if(!/^[A-Za-z0-9]{4,}$/.test(_memberId.value)) {
		alert("아이디는 영문자/숫자로 4글자 이상이어야 합니다.");
		return false;
	}
	if(idValid.value !== "1") {
		alert("아이디 중복검사를 해주세요.");
		return false;
	}
	if(!/^[A-Za-z0-9!@#$%^&*()]{4,}$/.test(_password.value)) {
		alert("비밀번호는 영문자/숫자/특수문자!@#$%^&*()로 4글자 이상이어야 합니다.");
		return false;
	}
	if(!passwordCheck.onblur()) {
		return false;
	}
	if(!/^[가-힣]{2,}$/.test(memberName.value)) {
		alert("이름은 한글 2글자 이상 입력해주세요.");
		return false;
	}
	if(!/^010\d{8}$/.test(phone.value)) {
		alert("유효한 전화번호를 입력하세요.");
		return false;
	}
};
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
