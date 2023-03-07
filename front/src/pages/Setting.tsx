import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import styled from 'styled-components';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Avatar from '@mui/material/Avatar';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import { IconButton } from '@mui/material';
import { MyInformation } from './Mypage';
import { MAIN_COLOR } from '../style/palette';
import { Header } from '../components/common';
import { DeleteAccount, ChangePassword } from '../components/setting';
import { interests } from '../components/common/interestsList';
import { modifyMypage } from '../api/user';

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
        root: {
          width: '7rem'
        }
      }
    }
  }
});

const TextFieldStyle = {
  input: { padding: '0.4rem' },
  ml: 4
}

export interface SettingData {
  nickname: string,
  introduction: string,
  genderType: string,
  ageType: string,
  categories: string[],
  profileImage?: string
}

const Setting = () => {
  const [profileData, setProfileData] = useState<MyInformation>();
  const [checked, setChecked] = useState<string[]>([]);
  const [imageSrc, setImageSrc] = useState<string>('');
  const [showDeleteModal, setShowDeleteModal] = useState<boolean>(false);
  const [showChangeModal, setShowChangeModal] = useState<boolean>(false);

  const location = useLocation();
  const navigation = useNavigate();

  useEffect(() => {
    if(location.state.profileData) {
      setProfileData(location.state.profileData);
      setImageSrc(location.state.profileData.profileImage);
      setChecked(location.state.profileData.categories);
    }
    else navigation('/login');
  }, []);

  const reader = new FileReader();
  const avatarChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    if(event.target.files && event.target.files[0]) {
      reader.onload = () => {
        const dataURL = reader.result;
        setImageSrc(String(dataURL));
      };
      reader.readAsDataURL(event.target.files[0]);
    }
  };

  const deleteImage = () => {
    setImageSrc('');
    (document.getElementById('avatar') as HTMLInputElement).value = '';
  };

  const handleSubmit = async(event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const settingData: SettingData = { nickname: '', introduction: '', genderType: '', ageType: 'TWENTIES', categories: [] };
    const formData = new FormData(event.currentTarget);
    const imageFile = formData.get('image');
    const entries = formData.entries();
    for (const pair of entries) {
      if(pair[0] === 'nickname') settingData.nickname = String(pair[1]);
      else if(pair[0] === 'introduction') settingData.introduction = String(pair[1]);
      else if(pair[0] === 'genderType') settingData.genderType = String(pair[1]);
      else if(pair[0] === 'categories') settingData.categories.push(String(pair[1]));
    }
    if(imageFile instanceof File && imageFile.name) await modifyMypage(settingData, imageFile);
    else await modifyMypage(settingData, imageSrc);
    navigation('/mypage');
  };

  const countCheck = (event: React.MouseEvent<HTMLInputElement>) => {
    if(checked.includes(event.currentTarget.id)) {
      const newChecked = checked.filter((element) => element !== event.currentTarget.id);
      setChecked(newChecked);
    }
    else if(checked.length === 3) {
      alert('3개까지 선택할 수 있습니다.');
      (document.getElementById(event.currentTarget.id) as HTMLInputElement).checked = false;
    }
    else setChecked([ ...checked, event.currentTarget.id ]);
  };

  return(
    <>
      <ThemeProvider theme={theme}>
        <Header />
        {profileData &&
        <Box component='form' encType='multipart/form-data' onSubmit={handleSubmit} sx={{ width: '24rem', margin: '3rem auto' }}>
          <StPhotoWrapper>
            <div>
              <input id='avatar' name='image' type='file' accept='image/*' onChange={avatarChange} hidden />
              <label htmlFor='avatar'>
                <IconButton component='span'>
                  <Avatar sx={{ width: 164, height: 164 }} src={imageSrc} />
                </IconButton>
              </label>
            </div>
            {imageSrc && <Button onClick={deleteImage}>사진 삭제</Button>}
          </StPhotoWrapper>
          <Box sx={{ mb: 2 }}>
            <Typography sx={{ fontSize: 18, fontWeight: 600 }} gutterBottom>기본정보</Typography>
            <StLabel>닉네임</StLabel>
            <TextField sx={TextFieldStyle} id='nickname' name='nickname' defaultValue={profileData.nickname} />
            <StLabel>한 줄 소개</StLabel>
            <TextField sx={TextFieldStyle} id='introduction' name='introduction' defaultValue={profileData.introduction} />
            <StChangePassword>
              <span onClick={() => setShowChangeModal(true)} style={{ textDecoration: 'underline' }}>비밀번호 변경</span>
            </StChangePassword>
          </Box>
          <Box>
            <Typography sx={{ fontSize: 18, fontWeight: 600 }}>상세정보</Typography>
            <StLabel>성별</StLabel>
            <select name='genderType' defaultValue={profileData.genderType}>
              <option value='MALE'>남성</option>
              <option value='FEMALE'>여성</option>
            </select>
            <Box>
              <StLabel>관심사</StLabel>
              {interests.map((interest) => (
                <StCheckItem key={interest[1]}>
                  <input
                    id={interest[1]}
                    type='checkbox'
                    name='categories'
                    value={interest[1]}
                    onClick={countCheck}
                    defaultChecked={profileData.categories.includes(interest[1])}
                  />{interest[0]}
                </StCheckItem>
              ))}
            </Box>
          </Box>
          <StButtonBox>
            <Button variant='contained' onClick={() => setShowDeleteModal(true)}>회원탈퇴</Button>
            <Button variant='contained' type='submit'>저장하기</Button>
          </StButtonBox>
        </Box>
        }
        <Dialog open={showDeleteModal} onClose={() => setShowDeleteModal(false)}>
          <DeleteAccount />
        </Dialog>
        <Dialog open={showChangeModal} onClose={() => setShowChangeModal(false)}>
          <ChangePassword show={setShowChangeModal} />
        </Dialog>
      </ThemeProvider>
    </>
  );
};

const StPhotoWrapper = styled.div`
  width: 100%;
  height: 16rem;
  text-align: center;
`;

const StLabel = styled.div`
  display: inline-block;
  width: 5.6rem;
  text-align: center;
  font-size: 1.1rem;
  height: 2.4rem;
`;

const StCheckItem = styled.span`
  margin: 0.4rem;
  line-height: 2.4rem;
  vertical-align: middle;
`;

const StButtonBox = styled.div`
  display: grid;
  grid-template-columns: 50% 50%;
  margin-top: 6rem;
  place-items: center;
`;

const StChangePassword = styled.div`
  width: 100%;
  text-align: center;
  font-size: 0.9rem;
  margin-top: 1.2rem;
`;

export default Setting;