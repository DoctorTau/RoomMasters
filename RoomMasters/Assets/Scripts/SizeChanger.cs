using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SizeChanger : MonoBehaviour
{
    [SerializeField] public GameObject leftTextBox;
    [SerializeField] public GameObject rightTextBox;
    [SerializeField] public GameObject errorMessage;

    public int leftLength = 0;
    public int rightLength = 0;
    public bool isAwailable = false;

    private void Awake()
    {
        // leftTextBox.GetComponent<InputField>().text = this.GetComponent<Floor>().GridSize.x.ToString();
        // rightTextBox.GetComponent<InputField>().text = this.GetComponent<Floor>().GridSize.x.ToString();
    }
    void Update()
    {
        string leftText = leftTextBox.GetComponent<InputField>().text;
        string rightText = rightTextBox.GetComponent<InputField>().text;
        if (string.IsNullOrEmpty(leftText)
           || string.IsNullOrEmpty(rightText)) return;
        if (leftText != "" && rightText != "" && (
            !int.TryParse(leftText, out int leftValue)
            || leftValue <= 0
            || !int.TryParse(rightText, out int rightValue)
            || rightValue <= 0))
        {
            errorMessage.SetActive(true);
            isAwailable = false;
            leftLength = rightLength = 0;
            return;
        }
        errorMessage.SetActive(false);
        isAwailable = true;
        leftLength = int.Parse(leftTextBox.GetComponent<InputField>().text);
        rightLength = int.Parse(rightTextBox.GetComponent<InputField>().text);

    }
}


