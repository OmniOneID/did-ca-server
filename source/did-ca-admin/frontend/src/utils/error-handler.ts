export const formatErrorMessage = (err: any, contextMessage: string): string => {
  let errorMessage = "An unknown error occurred.";

  if (err?.response) {
    errorMessage = `HTTP ${err.response.status}: ${err.response.data?.message || "Server error"}`;
  } else if (err?.message) {
    errorMessage = err.message;
  } else if (typeof err === "object") {
    errorMessage = JSON.stringify(err, null, 2);
  } else {
    errorMessage = String(err);
  }

  return `${contextMessage}\n\n${errorMessage}`;
};
