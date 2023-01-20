const maxQuery = (maxWidth: number): string => `@media (max-width: ${maxWidth}px)`;
const minQuery = (minWidth: number): string => `@media (min-width: ${minWidth}px)`;

const media = {
  mobile: maxQuery(992),
  desktop: minQuery(992)
};

export default media;