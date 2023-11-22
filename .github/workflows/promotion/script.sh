#branch=$(echo "$GITHUB_EVENT_PATH" | jq -r '.comment.body | capture("/perform-hotfix\\s*\\b(?<text>.+?)\\b") | .text')
if [ -z "$branch" ]; then
  branch="production"
fi
echo "Text after /perform-hotfix: $branch"
#echo "branch=$branch" >> $GITHUB_OUTPUT
