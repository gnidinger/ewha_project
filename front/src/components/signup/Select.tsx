import styled from 'styled-components';
import { interests } from './interestsList';
import { Button } from '../../style/Button';

const Select = () => {
  return(
    <SelectWrapper>
      <StComment>관심사를 선택해주세요 (복수 선택 가능)</StComment>
      {interests.map((interest, index) => (
        <StButton key={index}>{interest}</StButton>
      ))}
    </SelectWrapper>
  );
};

const SelectWrapper = styled.div`
  width: 100%;
`;

const StComment = styled.div`
  width: 70%;
  margin: 0 auto 4.8rem auto;
  font-size: 1.45rem;
  text-align: center;
`;

const StButton = styled(Button)`
  width: 44%;
  margin: 0.4rem 3%;
`;

export default Select;