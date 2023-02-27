import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import styled from 'styled-components';
import { Header } from '../components/common';
import { Section } from '../components/Home';
import { getCookie } from '../api/cookie';
import { MAIN_COLOR } from '../style/palette';
import { mypage } from '../api/user';

const Home = () => {
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  const [ariFactor, setAriFactor] = useState<number>(0);

  const navigation = useNavigate();

  useEffect(() => {
    if(getCookie('ari_login')) {
      setIsLoggedIn(true);
      mypage().then(data => setAriFactor(data.ariFactor));
    }  
  }, []);

  return(
    <StPageWrapper>
      <Header />
      {isLoggedIn && <Section title='오늘의 아리지수'></Section>}
      <Section title='아리랑 나누기'></Section>
      <StLink to='/ari'>
        <Section title='아리공간'></Section>
      </StLink>
    </StPageWrapper>
  );
};

const StPageWrapper = styled.div`
  position: relative;
  height: 100vh;
`;

const StLink = styled(Link)`
  text-decoration: none;
  color: black;
`;

const StAriFactorGraph = styled.div`
  width: 200px;
  height: 100px;
  border: 2px solid ${MAIN_COLOR};
  border-radius: 100px 100px 0px 0px;
`;

export default Home;