import { useState } from 'react';
import styled from 'styled-components';
import { Logo } from '../components/common';
import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { Main, SignupForm } from '../components/signup';
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
    MuiButton: {
      styleOverrides: {
        sizeSmall: {
          padding: '0.4rem',
          verticalAlign: 'bottom',
          marginLeft: '0.7rem'
        }
      }
    }
  }
});

const Signup = () => {
  const [showSignupForm, setShowSignupForm] = useState<boolean>(false);

  const goSignup = (): void => {
    setShowSignupForm(true);
  };

  return (
    <StPageWrapper>
      <ThemeProvider theme={theme}>
        <Container component="main">
          <CssBaseline />
          <LogoWrapper>
            <Logo />
          </LogoWrapper>
          {!showSignupForm && <Main clickSignup={goSignup} />}
          {showSignupForm && <SignupForm />}
        </Container>
      </ThemeProvider>
    </StPageWrapper>
  );
};

const StPageWrapper = styled.div`
  display: grid;
  place-items: center;
  width: 24rem;
  height: 100vh;
  margin: 0 auto;
`;

const LogoWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 10rem;
`;

export default Signup;