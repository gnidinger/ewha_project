import { useState, useEffect } from 'react';
import { Link, useLocation, useSearchParams } from 'react-router-dom';
import styled from 'styled-components';
import { Header } from '../components/common';
import { Section } from '../components/Home';
import { getCookie, setCookie } from '../api/cookie';
import { MAIN_COLOR } from '../style/palette';
import { mypage } from '../api/user';
import { Chart, ArcElement } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';
import { ChartData } from 'chart.js/dist/types/index';
Chart.register(ArcElement);

const Home = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  const [ariFactor, setAriFactor] = useState<number>(0);
  const [todaysQuestion, setTodaysQuestion] = useState<string>('');

  const location = useLocation();

  const chartData: ChartData<'doughnut'> = {
    datasets: [{
      data: [ariFactor, 100 - ariFactor],
      backgroundColor: [MAIN_COLOR, '#F0F0F0'],
      rotation: -90,
      circumference: 180
    }],
  };

  useEffect(() => {
    if(location.search) {
      setCookie('ari_login', searchParams.get('access_token'));
      setCookie('refreshToken', searchParams.get('refresh_token'));
      window.location.replace('/');
    }
    if(getCookie('ari_login')) {
      setIsLoggedIn(true);
      mypage().then(data => {
        if(data.isFirstLogin) window.location.replace('/first-setting');
        setAriFactor(data.ariFactor);
      });
    }  
  }, []);

  return(
    <StPageWrapper>
      <Header />
      {isLoggedIn &&
        <Section title='오늘의 아리지수'>
          <Doughnut data={chartData} />
        </Section>
      }
      <Section title='아리랑 나누기'>
        {todaysQuestion}
      </Section>
      <Section title='아리공간'>
        <StAri>
          <StLink to='/ari'>
            <StBoard>
              <StBoardIcon />
              <div>최신 글 보기</div>
            </StBoard>
          </StLink>
          <StLink to='/ari' state={{ latest: false }}>
            <StBoard>
              <StBoardIcon />
              <div>주제별 보기</div>
            </StBoard>
          </StLink>
        </StAri>
      </Section>
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

const StAri = styled.div`
  width: 100%;
  display: grid;
  grid-template-columns: 50% 50%;
  place-items: center;
  margin-top: 2rem;
`;

const StBoard = styled.div`
  display: grid;
  grid-template-rows: 70% 30%;
  place-items: center;
  width: 12rem;
  height: 16rem;
  border: 1px solid black;
`;

const StBoardIcon = styled.div`
  width: 8rem;
  height: 8rem;
  border: 1px solid black;
  border-radius: 50%;
`;

export default Home;