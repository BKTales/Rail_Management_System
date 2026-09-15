#ifndef GET_NEXT_LINE_H
# define GET_NEXT_LINE_H

# ifndef BUFFER_SIZE
#  define BUFFER_SIZE 1
# endif

# include <stdlib.h>
# include <unistd.h>

char	*trim_n(char *str);
char	*get_next_line(int fd);
char	*new_cache(char *cache);
char	*ft_strdup(const char *str);
char	*ft_strjoin(char *s1, char *s2);
char	*free_all(char *cache, char *buffer);
char	*when_found_nl(char *buffer, char *cache, int fd);

int		check_read(char *buffer, char *cache, int index, int flag);
int		search_for_nl(const char *str);
int		ft_strlen(const char *str);

#endif
