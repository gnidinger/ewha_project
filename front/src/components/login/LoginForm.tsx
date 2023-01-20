import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { Input } from '../../style/Input';
import { Button } from '../../style/Button';

const LoginForm = () => {
  const navigation = useNavigate();

  const clickSignupBtn = () => {
    navigation('/signup');
  };

  const KAKAO_AUTH_URL = 'http://localhost:8080/oauth2/authorization/kakao?redirect_uri=http://localhost:8080/login/oauth2/kakao';
  const NAVER_AUTH_URL = 'http://localhost:8080/oauth2/authorization/naver?redirect_uri=http://localhost:8080/login/ouath2/naver';

  return(
    <StForm>
      <StInput placeholder='아이디'></StInput>
      <StInput placeholder='비밀번호'></StInput>
      {/* <StLabel><StCheckBox type='checkbox' defaultChecked={true} />자동로그인</StLabel> */}
      <Button type='submit'>로그인</Button>
      <Button onClick={clickSignupBtn}>회원가입</Button>
      <StSNSLogin>
        SNS 간편 로그인
        <a href={NAVER_AUTH_URL}>
          <SocialIcon src='img/icon/naver_icon.png' />
        </a>
        <a href={KAKAO_AUTH_URL}>
          <SocialIcon src='img/icon/kakao_icon.png' />
        </a>
      </StSNSLogin>
    </StForm>
  );
};

const StForm = styled.form`
  width: 100%;
`;

const StInput = styled(Input)`
  margin-bottom: 1.6rem;
`;

// const StCheckBox = styled.input`
//   margin: 0 0.5rem 0 0;
//   vertical-align: bottom;
// `;

// const StLabel = styled.label`
//   display: inline-block;
//   margin-bottom: 4.6rem;
// `;

const StSNSLogin = styled.div`
  width: 55%;
  margin: 4.2rem auto 5.2rem auto;
  text-align: center;
  font-size: 1rem;
`;

const SocialIcon = styled.img`
  cursor: pointer;
  width: 2.8rem;
  height: 2.8rem;
  margin: 0.7rem;
`;

export default LoginForm;