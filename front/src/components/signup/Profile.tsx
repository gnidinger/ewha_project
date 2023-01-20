import styled from 'styled-components';
import { Photo } from '../common';
import { Input } from '../../style/Input';

const Profile = () => {
  return(
    <ProfileWrapper>
      <StComment>프로필을 설정해주세요</StComment>
      <StPhotoWrapper>
        <Photo />
      </StPhotoWrapper>
      <Input placeholder='별명(선택)'></Input>
    </ProfileWrapper>
  );
};

const ProfileWrapper = styled.div`
  width: 100%;
`;

const StComment = styled.div`
  width: 65%;
  margin: 0 auto 1.8rem auto;
  font-size: 1.45rem;
  text-align: center;
`;

const StPhotoWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 22rem;
  margin-bottom: 6.8rem;
`;

export default Profile;