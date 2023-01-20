import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Home, Login, Signup, Forgot, AriBoard, Mypage, SignupMui } from './pages';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/login' element={<Login />} />
          <Route path='/signup' element={<Signup />} />
          <Route path='/forgot' element={<Forgot />} />
          <Route path='/ari' element={<AriBoard />} />
          <Route path='/mypage' element={<Mypage />} />

          <Route path='/signup-mui' element={<SignupMui />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;