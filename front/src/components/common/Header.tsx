import styled from 'styled-components';

interface Props {
  showPrevIcon: boolean,
  children: string,
  bottomLine?: boolean
}

const Header = ({ showPrevIcon, children, bottomLine = false }: Props) => {
  return(
    <StHeader bottomLine={bottomLine}>
      {showPrevIcon &&
        <StPrev>
          <StPrevIcon src='img/icon/prev2.png' />
        </StPrev>
      }
      {children}
    </StHeader>
  );
};

const StHeader = styled.div`
  /* position: fixed; */
  width: 100%;
  height: 4.6rem;
  font-size: 1.6rem;
  text-align: center;
  line-height: 4.6rem;
  vertical-align: middle;
  border-bottom: ${({ bottomLine }: { bottomLine: boolean }) => bottomLine ? '0.1rem solid grey' : 'none'};
`;

const StPrev = styled.div`
  position: fixed;
  width: 4.6rem;
  height: 4.6rem;
  line-height: 4.6rem;
`;

const StPrevIcon = styled.img`
  width: 1.2rem;
  height: 2.0rem;
  vertical-align: middle;
`;

export default Header;