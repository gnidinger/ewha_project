import styled from 'styled-components';

const Photo = () => {
  return(
    <StPhoto></StPhoto>
  );
};

const StPhoto = styled.div`
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);
  width: 80%;
  height: 80%;
  border-radius: 50%;
  background-color: #F0F0F0;
`;

export default Photo;