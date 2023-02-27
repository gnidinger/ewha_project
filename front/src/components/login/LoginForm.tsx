import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Typography from '@mui/material/Typography';
import { Input } from '../../style/Input';
import { Button } from '../../style/Button';
import { login, notification } from '../../api/user';
import { getCookie } from '../../api/cookie';

const LoginForm = () => {
  const [showInvalid, setShowInvalid] = useState<boolean>(false);

  const navigation = useNavigate();

  const clickSignupBtn = () => {
    navigation('/signup');
  };

  const submit = async(event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const loginData = await login(data.get('id') as string, data.get('password') as string);
    if(loginData.isFirstLogin === false) {
      notification();
      window.location.replace('/');
    }
    else if(loginData.isFirstLogin === true) {
      notification();
      window.location.replace('/first-setting');
    }
    else if(loginData.message === 'Unauthorized') setShowInvalid(true);
  };

  useEffect(() => {
    if(getCookie('ari_login')) navigation('/');
  }, []);

  return(
    <StForm onSubmit={submit}>
      <StInput autoComplete='id' id='id' name='id' placeholder='아이디'></StInput>
      <StInput id='password' name='password' type='password' placeholder='비밀번호'></StInput>
      {showInvalid && <Typography>아이디, 비밀번호를 다시 확인해주세요.</Typography>}
      <Button type='submit'>로그인</Button>
      <Button onClick={clickSignupBtn}>회원가입</Button>
      <StSNSLogin>
        SNS 간편 로그인
        <a href={`${process.env.REACT_APP_API_URL}/oauth2/authorization/naver?redirect_uri=http://localhost:3000/login`}>
          <SocialIcon src='img/icon/naver_icon.png' />
        </a>
        <a href={`${process.env.REACT_APP_API_URL}/oauth2/authorization/kakao?redirect_uri=http://localhost:3000/login`}>
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