export let StringExtensions = {
    EMPTY: "",
    isNullOrWhitespace: function(str: string): boolean {
        return (!str || (str.length === 0) || !str.trim());
    }
};