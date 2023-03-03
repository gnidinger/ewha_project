import styled from 'styled-components';
import Box from '@mui/material/Box';
import { Button } from '../../style/Button';

interface Props {
  clickSignup: () => void
}

const Main = ({ clickSignup }: Props) => {
  return(
    <Box
      sx={{
        marginTop: 6,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      }}
    >
      <Button fontColor={true} backgroundColor='#FEE500'>
        <a href={`${process.env.REACT_APP_API_URL}/oauth2/authorization/kakao?redirect_uri=${process.env.REACT_APP_REDIRECT_URI}`}>
          <SocialLogo src='img/icon/kakao_icon_square.PNG' />카카오로 시작하기
        </a>
      </Button>
      <Button backgroundColor='#03C75A'>
        <a href={`${process.env.REACT_APP_API_URL}/oauth2/authorization/naver?redirect_uri=${process.env.REACT_APP_REDIRECT_URI}`}>
          <SocialLogo src='img/icon/naver_icon.png' />네이버로 시작하기
        </a>
      </Button>
      <Button fontColor={true} backgroundColor='F0F0F0' onClick={clickSignup}>새 계정 만들기</Button>
    </Box>
  );
};

const SocialLogo = styled.img`
  float: left;
  height: 2.8rem;
`;

export default Main;