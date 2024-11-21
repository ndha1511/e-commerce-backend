export const valueOf = <T extends { [key: string]: string | number }>(
    enumObj: T,
    value: string
  ): T[keyof T] | undefined => {
    return Object.values(enumObj).includes(value as T[keyof T]) ? (value as T[keyof T]) : undefined;
  };