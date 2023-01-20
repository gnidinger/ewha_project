import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { SignupContext } from '../../pages/Signup';
import styled from 'styled-components';
import { Logo } from '../common';
import { Button } from '../../style/Button';

const Main = () => {
  const { upStep } = useContext(SignupContext);
  const navigation = useNavigate();

  const clickSignupBtn = () => {
    navigation('/signup-mui');
  };

  return(
    <MainWrapper>
      <LogoWrapper>
        <Logo />
      </LogoWrapper>
      <Button fontColor={true} backgroundColor='#FEE500'>
        <SocialLogo src='img/icon/kakao_icon_square.PNG' />카카오로 시작하기
      </Button>
      <Button backgroundColor='#03C75A'>
        <SocialLogo src='img/icon/naver_icon.png' />네이버로 시작하기
      </Button>
      <Button fontColor={true} backgroundColor='F0F0F0' onClick={clickSignupBtn}>새 계정 만들기</Button>
    </MainWrapper>
  );
};

const MainWrapper = styled.div`
  width: 100%;
`;

const LogoWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 18rem;
`;

const SocialLogo = styled.img`
  float: left;
  height: 2.8rem;
`;

export default Main;