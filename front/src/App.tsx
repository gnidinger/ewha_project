import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Home, Login, Forgot, AriBoard, Mypage, Signup, SetInfo, Write, View, Setting, Notice, Message } from './pages';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { MAIN_COLOR } from './style/palette';

const theme = createTheme({
  palette: {
    primary: {
      main: MAIN_COLOR
    },
    secondary: {
      main: '#F0F0F0'
    }
  }
});

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <ThemeProvider theme={theme}>
          <Routes>
            <Route path='/' element={<Home />} />
            <Route path='/login' element={<Login />} />
            <Route path='/signup' element={<Signup />} />
            <Route path='/forgot' element={<Forgot />} />
            <Route path='/ari' element={<AriBoard />} />
            <Route path='/mypage' element={<Mypage />} />
            <Route path='/first-setting' element={<SetInfo />} />
            <Route path='/write' element={<Write />} />
            <Route path='/setting' element={<Setting />} /> 
            <Route path='/post/:post' element={<View />} />
            <Route path='/notice' element={<Notice />} />
            <Route path='/message' element={<Message />} />
          </Routes>
        </ThemeProvider>
      </BrowserRouter>
    </div>
  );
}

export default App;