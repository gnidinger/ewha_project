import styled from 'styled-components';
import { Input } from '../../style/Input';

const Certification = () => {
  return(
    <StForm>
      <StComment>서비스 이용을 위해 정보를 입력해주세요</StComment>
      <StInput placeholder='이름' />
      <StInput placeholder='휴대전화' />
      <StInput placeholder='인증번호' />
    </StForm>
  );
};

const StForm = styled.form`
  width: 100%;
`;

const StComment = styled.div`
  width: 58%;
  margin: 0 auto 6.8rem auto;
  font-size: 1.45rem;
`;

const StInput = styled(Input)`
  margin-bottom: 2.6rem;
`;

export default Certification;