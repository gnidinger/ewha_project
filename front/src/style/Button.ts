import styled from 'styled-components';

interface Props {
  fontColor?: boolean,
  backgroundColor?: string
}

export const Button = styled.button<Props>`
  cursor: pointer;
  width: 100%;
  height: 3rem;
  line-height: 3rem;
  vertical-align: middle;
  color: ${({ fontColor }) => fontColor ? 'black' : 'white'};
  font-weight: 600;
  background-color: ${({ backgroundColor }) => backgroundColor ? backgroundColor : '#E95A54'};
  margin: 0.4rem 0;
  border: none;
  border-radius: 12px;
`;

export const SubButton = styled.button`
  cursor: pointer;
  padding: 0 3px;
  border: none;
  background-color: #F0F0F0;
`;