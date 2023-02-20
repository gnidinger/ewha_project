import styled from 'styled-components';
import { Input } from '../../style/Input';
import { Button } from '../../style/Button';

const IdForm = () => {
  return(
    <StForm>
      <StInput placeholder='이름'></StInput>
      <StInput placeholder='휴대전화'></StInput>
      <Button>인증번호 받기</Button>
    </StForm>
  );
};

const StForm = styled.form`
  width: 90%;
  margin: 3.6rem auto;
`;

const StInput = styled(Input)`
  margin-bottom: 2.2rem;
`;

export default IdForm;