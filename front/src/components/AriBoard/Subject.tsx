import styled from 'styled-components';

interface Props {
  children: string
}

const Subject = ({ children }: Props) => {
  return(
    <SubjectWrapper>{children}</SubjectWrapper>
  );
};

const SubjectWrapper = styled.div`
  width: 28vw;
  height: 28vw;
  border-radius: 50%;
  border: 0.1rem solid grey;
  text-align: center;
  line-height: 28vw;
  vertical-align: middle;
  margin: auto;
  font-size: 1.35rem;
`;

export default Subject;