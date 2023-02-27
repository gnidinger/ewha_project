import styled from 'styled-components';

interface Props {
  title: string,
  children?: any
}

const Section = ({ title, children }: Props) => {
  return(
    <SectionWrapper>
      <StTitle>{title}</StTitle>
      <StContent>{children}</StContent>
    </SectionWrapper>
  );
};

const SectionWrapper = styled.div`
  width: 100%;
  border-bottom: 0.1rem solid grey;
  padding: 1.8rem;
  font-size: 1.2rem;
`;

const StTitle = styled.div`
`;

const StContent = styled.div`
  display: grid;
  place-items: center;
`;

export default Section;