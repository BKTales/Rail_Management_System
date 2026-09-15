#include "get_next_line.h"

int	ft_strlen(const char *str)
{
	int	i;

	i = 0;
	while (str[i] != '\0')
		i++;
	return (i);
}

char	*ft_strjoin(char *s1, char *s2)
{
	char	*joined_str;
	int		j;
	int		i;
	int		total_length;

	j = 0;
	i = 0;
	total_length = ft_strlen(s1) + ft_strlen(s2);
	joined_str = (char *)malloc((total_length + 1));
	if (!joined_str)
		return (NULL);
	while (s1[i] != '\0')
	{
		joined_str[i] = s1[i];
		i++;
	}
	while (s2[j] != '\0')
	{
		joined_str[i + j] = s2[j];
		j++;
	}
	joined_str[i + j] = '\0';
	free(s1);
	return (joined_str);
}

char	*ft_strdup(const char *str)
{
	char	*duplicated;
	int		many_characters;
	int		i;

	i = 0;
	many_characters = ft_strlen(str);
	duplicated = (char *)malloc((many_characters + 1));
	if (!duplicated)
		return (NULL);
	while (i < many_characters)
	{
		duplicated[i] = str[i];
		i++;
	}
	duplicated[i] = '\0';
	return (duplicated);
}

char	*trim_n(char *str)
{
	int	i;

	i = 0;
	while (str[i] != '\0')
	{
		if (str[i] == '\n')
		{
			while (str[i++] != '\0')
				str[i] = '\0';
			break ;
		}
		i++;
	}
	return (str);
}

int	search_for_nl(const char	*str)
{
	int	i;

	i = 0;
	while (str[i])
	{
		if (str[i] == '\n')
			return (1);
		i++;
	}
	return (0);
}
