/* Copyright (c) 2012, BuildmLearn Contributors listed at http://buildmlearn.org/people/
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

 * Neither the name of the BuildmLearn nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.buildmlearn.quiztemplate.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.buildmlearn.quiztemplate.R;
import com.buildmlearn.quiztemplate.objects.GlobalData;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends BaseActivity {

    private GlobalData gd;
    private TextView iQuestion_no_Label;
    private TextView iQuestionLabel;
    private RadioButton iRad1, iRad2, iRad3, iRad0;
    private LinearLayout iSubmitButton, iNextButton;
    private List<RadioButton> iRadButtonList = new ArrayList<RadioButton>();
    private int iQuestionIndex = 0;
    private int iCurrentCorrectAnswer;
    private RadioGroup iRadioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        gd = GlobalData.getInstance();

        iQuestion_no_Label = (TextView) findViewById(R.id.question_no);
        iQuestionLabel = (TextView) findViewById(R.id.question_label);

        iRad0 = (RadioButton) findViewById(R.id.radio0);
        iRad1 = (RadioButton) findViewById(R.id.radio1);
        iRad2 = (RadioButton) findViewById(R.id.radio2);
        iRad3 = (RadioButton) findViewById(R.id.radio3);

        iRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

        iRadButtonList.add(iRad0);
        iRadButtonList.add(iRad1);
        iRadButtonList.add(iRad2);
        iRadButtonList.add(iRad3);

        iSubmitButton = (LinearLayout) findViewById(R.id.submit_button);
        iSubmitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int selectedAnswer = getSelectedAnswer();
                if (selectedAnswer == -1) {
                    makeToast("Please select an answer!");
                } else if (selectedAnswer != -1
                        && selectedAnswer == iCurrentCorrectAnswer) {
                    iRadButtonList.get(iCurrentCorrectAnswer)
                            .setBackgroundColor(Color.GREEN);
                    makeToast("That's the correct answer!");
                    gd.setCorrect(gd.getCorrect() + 1);
                    iSubmitButton.setEnabled(false);
                    /*
                     * iSubmitButton.setVisibility(View.GONE);
					 * iNextButton.setVisibility(View.VISIBLE);
					 */
                } else {
                    iRadButtonList.get(selectedAnswer).setBackgroundColor(
                            Color.RED);
                    iRadButtonList.get(iCurrentCorrectAnswer)
                            .setBackgroundColor(Color.GREEN);
                    makeToast("Sorry, wrong answer!");

                    iSubmitButton.setEnabled(false);
                    gd.setWrong(gd.getWrong() + 1);
                    // iSubmitButton.setVisibility(View.GONE);
                    // iNextButton.setVisibility(View.VISIBLE);

                }
            }
        });

        iNextButton = (LinearLayout) findViewById(R.id.next_button);
        iNextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // set all radios to white
                for (int i = 0; i < iRadButtonList.size(); i++) {
                    iRadButtonList.get(i).setBackgroundColor(Color.TRANSPARENT);
                }

                // Increase the index to next ques
                iQuestionIndex = iQuestionIndex + 1;

                if (iQuestionIndex < gd.getModel().size()) {
                    populateQuestion(iQuestionIndex);

                    iSubmitButton.setEnabled(true);
                    // iNextButton.setVisibility(View.GONE);
                } else {
                    // if the quiz is over
                    reInitialize();
                    Intent myIntent = new Intent(arg0.getContext(),
                            ScoreActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        });
        // iNextButton.setVisibility(View.GONE);

        populateQuestion(iQuestionIndex);

    }

    public void radioClick(View v) {

    }

    public void populateQuestion(int index) {
        for (int i = 0; i < iRadButtonList.size(); i++) {
            iRadButtonList.get(i).setBackgroundColor(Color.TRANSPARENT);
            iRadButtonList.get(i).setChecked(false);
            iRadButtonList.get(i).setVisibility(View.GONE);
        }

        iQuestion_no_Label.setText("Question #" + String.valueOf(index + 1)
                + " of " + gd.getTotal());
        iQuestionLabel.setText(gd.getModel().get(index).getQuestion());
        ArrayList<String> options = gd.getModel().get(index).getOptions();
        for (int i = 0; i < options.size(); i++) {
            iRadButtonList.get(i).setText(options.get(i));
            iRadButtonList.get(i).setVisibility(View.VISIBLE);
        }
        iCurrentCorrectAnswer = Integer
                .parseInt(gd.getModel().get(index).getAnswer());

		/*
        String[] ques_content = gd.iQuizList.get(index).split("==");

		String question = ques_content[0];
		iQuestionLabel.setText(question);

		for (int i = 1; i < ques_content.length - 1; i++) {
			iRadButtonList.get(i - 1).setText(ques_content[i]);
			iRadButtonList.get(i - 1).setVisibility(View.VISIBLE);
		}

		iCurrentCorrectAnswer = Integer
				.parseInt(ques_content[ques_content.length - 1]);
*/
    }

    public int getSelectedAnswer() {
        int selected = -1;
        for (int i = 0; i < iRadButtonList.size(); i++) {
            if (iRadButtonList.get(i).isChecked()) {
                return i;
            }
        }
        return selected;
    }

    public void reInitialize() {

        iQuestionIndex = 0;
        gd.getiQuizList().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            showDialofForAboutBuildmLearn();

            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_enter_from_left_animation, R.anim.activity_exit_to_right_animation);
    }
}