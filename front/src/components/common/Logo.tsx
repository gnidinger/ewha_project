import styled from 'styled-components';

const Logo = () => {
  return(<StLogo src='img/logo/logo1.PNG'></StLogo>);
};

const StLogo = styled.img`
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);
  width: 50%;
`;

export default Logo;