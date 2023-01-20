import { useState, createContext } from 'react';
import styled from 'styled-components';
import { Logo } from '../components/common';
import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { SignupFormMui } from '../components/signup';

const theme = createTheme({
  palette: {
    primary: {
      main: '#E95A54'
    },
    secondary: {
      main: '#F0F0F0'
    }
  },
  components: {
    MuiContainer: {
      styleOverrides: {
        root: {
          maxWidth: '22rem'
        }
      }
    },
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

const SignupMui = () => {
  return (
    <StPageWrapper>
      <ThemeProvider theme={theme}>
        <Container component="main">
          <CssBaseline />
          <LogoWrapper>
            <Logo />
          </LogoWrapper>
          <SignupFormMui />
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

export default SignupMui;