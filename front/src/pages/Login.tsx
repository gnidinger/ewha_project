import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { Logo } from '../components/common';
import { LoginForm } from '../components/login';

const Login = () => {
  const navigation = useNavigate();

  const clickFind = () => {
    navigation('/forgot');
  };

  return(
    <StPageWrapper>
      <StMainWrapper>
        <StLogoWrapper>
          <Logo />
        </StLogoWrapper>
        <LoginForm />
        <StLink>
          <span onClick={clickFind} style={{ cursor: 'pointer' }}>아이디 / 비밀번호 찾기</span>
        </StLink>
      </StMainWrapper>
    </StPageWrapper>
  );
};

const StPageWrapper = styled.div`
  display: grid;
  place-items: center;
  height: 100vh;
`;

const StMainWrapper = styled.div`
  width: 19rem;
  margin: 0 auto;
`;

const StLogoWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 18rem;
`;

const StLink = styled.div`
  text-align: center;
  margin-top: 1.4rem;
  font-size: 0.8rem;
`;

export default Login;