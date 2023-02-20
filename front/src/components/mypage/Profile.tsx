import { Link } from 'react-router-dom';
import styled from 'styled-components';
import Avatar from '@mui/material/Avatar';
import { MAIN_COLOR } from '../../style/palette';
import { MyInformation } from '../../pages/Mypage';

interface Props {
  profileData: MyInformation
}

const Profile = ({ profileData }: Props) => {
  return(
    <ProfileWrapper>
      <StPhotoWrapper>
        <Avatar src={profileData.profileImage} sx={{ width: 164, height: 164, margin: '0 auto' }} />
      </StPhotoWrapper>
      <StName>{profileData.nickname}</StName>
      <StGaugeBar gaugeNum={profileData.ariFactor}>
        <div id='gauge' />
      </StGaugeBar>
      <StAriGauge>아리지수 {profileData.ariFactor}</StAriGauge>
      <StSetting>
        <Link to='/setting' state={{ profileData }}>내 정보 수정</Link>
      </StSetting>
    </ProfileWrapper>
  );
};

interface GaugeBar {
  gaugeNum: number
}

const ProfileWrapper = styled.div`
  width: 100%;
`;

const StPhotoWrapper = styled.div`
  width: 100%;
  height: 16rem;
  text-align: center;
`;

const StName = styled.div`
  width: 100%;
  text-align: center;
  font-size: 1.6rem;
  font-weight: 600;
`;

const StGaugeBar = styled.div<GaugeBar>`
  width: 100%;
  height: 0.6rem;
  border-radius: 20%;
  background-color: #D9D9D9;
  margin: 1.3rem 0 0.6rem 0;

  #gauge {
    width: ${({ gaugeNum }) => `${gaugeNum}%`};
    height: 100%;
    border-radius: 20%;
    background-color: ${MAIN_COLOR};
  }
`;

const StAriGauge = styled.div`
  width: 100%;
  text-align: center;
  font-size: 1.2rem;
`;

const StSetting = styled.div`
  width: 100%;
  text-align: center;
  font-size: 0.9rem;
  margin-top: 1.2rem;
`;

export default Profile;