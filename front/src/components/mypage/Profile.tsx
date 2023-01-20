import styled from 'styled-components';
import { Photo } from '../common';

interface Props {
  name: string,
  arigauge: number
}

const Profile = ({ name, arigauge }: Props) => {
  return(
    <ProfileWrapper>
      <StPhotoWrapper>
        <Photo />
      </StPhotoWrapper>
      <StName>{name}</StName>
      <StGaugeBar gaugeNum={arigauge}>
        <div id='gauge' />
      </StGaugeBar>
      <StAriGauge>아리지수 {arigauge}</StAriGauge>
      <StSetting>
        <a href='/login'>내 정보 수정</a>
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
  position: relative;
  width: 100%;
  height: 22rem;
`;

const StName = styled.div`
  width: 100%;
  text-align: center;
  font-size: 2.2rem;
  font-weight: 600;
`;

const StGaugeBar = styled.div<GaugeBar>`
  width: 100%;
  height: 0.6rem;
  border-radius: 20%;
  background-color: #D9D9D9;
  margin: 1.6rem 0 0.8rem 0;

  #gauge {
    width: ${({ gaugeNum }) => `${gaugeNum}%`};
    height: 100%;
    border-radius: 20%;
    background-color: #E95A54;
  }
`;

const StAriGauge = styled.div`
  width: 100%;
  text-align: center;
  font-size: 1.6rem;
`;

const StSetting = styled.div`
  width: 100%;
  text-align: center;
  font-size: 1.2rem;
  margin-top: 1.6rem;
`;

export default Profile;