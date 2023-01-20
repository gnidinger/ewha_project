import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import styled from 'styled-components';
import { Header } from '../components/common';
import { Section } from '../components/Home';

const Home = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const navigation = useNavigate();

  const goLoginPage = () => {
    navigation('/login');
  };

  useEffect(() => {
    if(isLoggedIn === false) goLoginPage();
  }, []);

  return(
    <StPageWrapper>
      <Header showPrevIcon={false} bottomLine={true}>아끼리</Header>
      <Section title='오늘의 아리지수'></Section>
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

export default Home;