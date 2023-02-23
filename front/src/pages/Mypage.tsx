import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { Profile, Activity } from '../components/mypage';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { mypage } from '../api/user';
import { Header } from '../components/common';
import { MAIN_COLOR } from '../style/palette';

const theme = createTheme({
  palette: {
    primary: {
      main: MAIN_COLOR
    },
    secondary: {
      main: '#F0F0F0'
    }
  },
  components: {
    MuiToggleButtonGroup: {
      styleOverrides: {
        root: {
          margin: '1rem auto'
        }
      }
    },
    MuiToggleButton: {
      styleOverrides: {
        root: {
          padding: '0.6rem'
        }
      }
    }
  }
});

export interface MyInformation {
  ageType: string,
  ariFactor: number,
  categories: string[],
  genderType: string,
  introduction: string,
  nickname: string,
  profileImage: string,
  userId: string
}

const Mypage = () => {
  const [myInfo, setMyInfo] = useState<MyInformation>();

  const navigation = useNavigate();

  const getMydata = async() => {
    const data = await mypage();
    if(data.error === 'Unauthorized' || data.message === 'Unauthorized') {
      navigation('/login');
    }
    setMyInfo(data);
  };

  useEffect(() => {
    getMydata();
  }, []);

  return(myInfo ?
    <>
      <Header />
      <ThemeProvider theme={theme}>
        <StMainWrapper>
          <Profile profileData={myInfo} />
          <Activity />
        </StMainWrapper>
      </ThemeProvider>
    </> :
    <></>
  );
};

const StMainWrapper = styled.div`
  width: 21rem;
  margin: 3rem auto;
`;

export default Mypage;